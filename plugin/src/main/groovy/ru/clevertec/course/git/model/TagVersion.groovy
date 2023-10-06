package ru.clevertec.course.git.model

import java.math.RoundingMode
import java.util.regex.Matcher
import java.util.regex.Pattern

final class TagVersion {
    private final static String VERSION_PATTERN = "^v(\\d+\\.\\d+)(%s|%s)?\$"
            .formatted(VersionType.STAGE.getPostfix(), VersionType.OTHER.getPostfix());

    final BigDecimal version
    final VersionType versionType

    TagVersion(BigDecimal version, VersionType versionType) {
        this.version = version
        this.versionType = versionType
    }

    BigDecimal getVersion() {
        return version
    }

    VersionType getVersionType() {
        return versionType
    }

    BigDecimal incrementMajorVersion() {
        BigDecimal integerPart = version.setScale(0, RoundingMode.DOWN)
        integerPart = integerPart.add(BigDecimal.ONE)
        return integerPart.setScale(version.scale(), RoundingMode.DOWN)
    }

    BigDecimal incrementMinorVersion() {
        return version.add(BigDecimal.valueOf(0.1))
    }

    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("v")
        sb.append(version)
        sb.append(versionType.getPostfix())
        return sb.toString()
    }

    static Optional<TagVersion> parseFromString(String str) {

        Pattern pattern = Pattern.compile(VERSION_PATTERN)
        Matcher matcher = pattern.matcher(str)
        if (matcher.matches()) {
            String versionNumber = matcher.group(1)
            String postfix = matcher.group(2)

            BigDecimal v = new BigDecimal(versionNumber)
            VersionType type = VersionType.DEV_OR_QA
            if (postfix != null) {
                switch (postfix) {
                    case VersionType.OTHER.getPostfix(): {
                        type = VersionType.OTHER
                        break
                    }
                    case VersionType.STAGE.getPostfix(): {
                        type = VersionType.STAGE
                        break
                    }
                }

            } else if (v.subtract(v.setScale(0, RoundingMode.DOWN)) == BigDecimal.ZERO) {
                type = VersionType.MASTER
            }

            return Optional.of(new TagVersion(v, type))
        }
        return Optional.empty()
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof TagVersion)) return false

        TagVersion that = (TagVersion) o

        if (version != that.version) return false
        if (versionType != that.versionType) return false

        return true
    }

    int hashCode() {
        int result
        result = (version != null ? version.hashCode() : 0)
        result = 31 * result + (versionType != null ? versionType.hashCode() : 0)
        return result
    }
}


